using Hypnode.Async;
using Hypnode.Core;
using Hypnode.Logic.Utils;
using Hypnode.System.Common;

namespace Hypnode.Logic.Compound
{
    public class FullAdderByte : INode
    {
        private IConnection<byte>? aPort = null;
        private IConnection<byte>? bPort = null;

        private IConnection<byte>? sum = null;

        public FullAdderByte SetInput(string portName, IConnection<byte> connection)
        {

            if (portName == "INA") aPort = connection;
            if (portName == "INB") bPort = connection;
            return this;
        }

        public FullAdderByte SetOutput(string portName, IConnection<byte> connection)
        {
            if (portName == "OUTSUM") sum = connection;
            return this;
        }

        public async Task ExecuteAsync()
        {
            if (aPort is null)
                throw new InvalidOperationException("Input port A is not set");

            if (bPort is null)
                throw new InvalidOperationException("Input port B is not set");

            while (aPort.TryReceive(out var a) && bPort.TryReceive(out var b))
            {
                var graph = new AsyncNodeGraph();
                var aIn = graph.CreateConnection<byte>();
                var bIn = graph.CreateConnection<byte>();
                var cIn = graph.CreateConnection<LogicValue>();

                graph.AddNode(new PulseValue<byte>(a))
                    .SetOutput("OUT", aIn);

                graph.AddNode(new PulseValue<byte>(b))
                    .SetOutput("OUT", bIn);

                graph.AddNode(new PulseValue<LogicValue>(LogicValue.False))
                    .SetOutput("OUT", cIn);

                var aDemux = graph.AddNode(new ByteDemux())
                    .SetInput("IN", aIn);

                var bDemux = graph.AddNode(new ByteDemux())
                    .SetInput("IN", bIn);

                IConnection<LogicValue>? carry = null;
                var sumWires = new IConnection<LogicValue>[8];

                for (int i = 0; i < 8; ++i)
                {
                    var adder = graph.AddNode(new FullAdder());

                    if (i == 0)
                    {
                        var aWire = graph.CreateConnection<LogicValue>();
                        var bWire = graph.CreateConnection<LogicValue>();
                        carry = graph.CreateConnection<LogicValue>();

                        aDemux.SetOutput(i, aWire);
                        bDemux.SetOutput(i, bWire);

                        adder.SetInput("INA", aWire);
                        adder.SetInput("INB", bWire);
                        adder.SetInput("INC", cIn);

                        var sumWire = graph.CreateConnection<LogicValue>();
                        sumWires[i] = sumWire;

                        adder.SetOutput("OUTSUM", sumWire);
                        adder.SetOutput("OUTC", carry);
                    }
                    else
                    {
                        var aWire = graph.CreateConnection<LogicValue>();
                        var bWire = graph.CreateConnection<LogicValue>();

                        aDemux.SetOutput(i, aWire);
                        bDemux.SetOutput(i, bWire);

                        adder.SetInput("INA", aWire);
                        adder.SetInput("INB", bWire);
                        adder.SetInput("INC", carry!);

                        carry = graph.CreateConnection<LogicValue>();

                        var sumWire = graph.CreateConnection<LogicValue>();
                        sumWires[i] = sumWire;

                        adder.SetOutput("OUTSUM", sumWire);
                        adder.SetOutput("OUTC", carry!);
                    }
                }

                var sumMux = graph.AddNode(new ByteMultiplexer());
                var resultWire = graph.CreateConnection<byte>();

                for (int i = 0; i < 8; ++i)
                    sumMux.SetInput(i, sumWires[i]);

                sumMux.SetOutput("OUT", resultWire);

                var result = graph.AddNode(new Register<byte>())
                    .SetInput("IN", resultWire);

                graph.AddNode(new VoidSink<LogicValue>())
                    .AddInput(carry!);

                await graph.EvaluateAsync();

                sum?.Send(result.GetValue());
            }

            sum?.Close();
        }
    }
}