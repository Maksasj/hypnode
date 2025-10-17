using Hypnode.Async;
using Hypnode.Core;
using Hypnode.Logic.Utils;
using Hypnode.System.Common;

namespace Hypnode.Logic.Compound
{
    public class FullAdderByte : ICompoundNode
    {
        private Connection<byte>? aPort = null;
        private Connection<byte>? bPort = null;
        private Connection<byte>? sum = null;

        public FullAdderByte(INodeGraph nodeGraph) : base(nodeGraph)
        {

        }

        public override INode SetPort(string portName, IConnection connection)
        {

            if (portName == "INA" && connection is Connection<byte> conn0) aPort = conn0;
            if (portName == "INB" && connection is Connection<byte> conn1) bPort = conn1;
            if (portName == "OUTSUM" && connection is Connection<byte> conn2) sum = conn2;

            return this;
        }

        public override async Task ExecuteAsync()
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
                    .SetPort("OUT", aIn);

                graph.AddNode(new PulseValue<byte>(b))
                    .SetPort("OUT", bIn);

                graph.AddNode(new PulseValue<LogicValue>(LogicValue.False))
                    .SetPort("OUT", cIn);

                var aDemux = graph.AddNode(new ByteSplitterIn())
                    .SetPort("IN", aIn);

                var bDemux = graph.AddNode(new ByteSplitterIn())
                    .SetPort("IN", bIn);

                Connection<LogicValue>? carry = null;
                var sumWires = new Connection<LogicValue>[8];

                for (int i = 0; i < 8; ++i)
                {
                    var adder = graph.AddNode(new FullAdder(new AsyncNodeGraph()));

                    if (i == 0)
                    {
                        var aWire = graph.CreateConnection<LogicValue>();
                        var bWire = graph.CreateConnection<LogicValue>();
                        carry = graph.CreateConnection<LogicValue>();

                        aDemux.SetPort(i.ToString(), aWire);
                        bDemux.SetPort(i.ToString(), bWire);

                        adder.SetPort("INA", aWire);
                        adder.SetPort("INB", bWire);
                        adder.SetPort("INC", cIn);

                        var sumWire = graph.CreateConnection<LogicValue>();
                        sumWires[i] = sumWire;

                        adder.SetPort("OUTSUM", sumWire);
                        adder.SetPort("OUTC", carry);
                    }
                    else
                    {
                        var aWire = graph.CreateConnection<LogicValue>();
                        var bWire = graph.CreateConnection<LogicValue>();

                        aDemux.SetPort(i.ToString(), aWire);
                        bDemux.SetPort(i.ToString(), bWire);

                        adder.SetPort("INA", aWire);
                        adder.SetPort("INB", bWire);
                        adder.SetPort("INC", carry!);

                        carry = graph.CreateConnection<LogicValue>();

                        var sumWire = graph.CreateConnection<LogicValue>();
                        sumWires[i] = sumWire;

                        adder.SetPort("OUTSUM", sumWire);
                        adder.SetPort("OUTC", carry!);
                    }
                }

                var sumMux = graph.AddNode(new ByteSplitterOut());
                var resultWire = graph.CreateConnection<byte>();

                for (int i = 0; i < 8; ++i)
                    sumMux.SetPort(i.ToString(), sumWires[i]);

                sumMux.SetPort("OUT", resultWire);

                var result = new Register<byte>();
                graph.AddNode(result).SetPort("IN", resultWire);

                graph.AddNode(new VoidSink<LogicValue>())
                    .SetPort("_", carry!);

                await graph.EvaluateAsync();

                sum?.Send(result.GetValue());
            }

            sum?.Close();
        }
    }
}