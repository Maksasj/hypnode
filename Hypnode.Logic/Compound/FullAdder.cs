using Hypnode.Async;
using Hypnode.Core;
using Hypnode.Logic.Gates;
using Hypnode.System.Common;

namespace Hypnode.Logic.Compound
{
    public class FullAdder : ICompoundNode
    {
        private Connection<LogicValue>? aPort = null;
        private Connection<LogicValue>? bPort = null;
        private Connection<LogicValue>? carryIn = null;

        private Connection<LogicValue>? sum = null;
        private Connection<LogicValue>? carryOut = null;

        public FullAdder(INodeGraph nodeGraph) : base(nodeGraph)
        {

        }

        public FullAdder SetInput(string portName, Connection<LogicValue> connection)
        {

            if (portName == "INA") aPort = connection;
            if (portName == "INB") bPort = connection;
            if (portName == "INC") carryIn = connection;
            return this;
        }

        public FullAdder SetOutput(string portName, Connection<LogicValue> connection)
        {
            if (portName == "OUTSUM") sum = connection;
            if (portName == "OUTC") carryOut = connection;
            return this;
        }

        public override async Task ExecuteAsync()
        {

            if (aPort is null)
                throw new InvalidOperationException("Input port A is not set");

            if (bPort is null)
                throw new InvalidOperationException("Input port B is not set");

            if (carryIn is null)
                throw new InvalidOperationException("Input port carryIn is not set");

            while (aPort.TryReceive(out var a) && bPort.TryReceive(out var b) && carryIn.TryReceive(out var c))
            {
                var graph = new AsyncNodeGraph();
                var AtoDemux1 = graph.CreateConnection<LogicValue>();
                var Demux1toXor1 = graph.CreateConnection<LogicValue>();
                var BtoDemux2 = graph.CreateConnection<LogicValue>();
                var Demux2toXor1 = graph.CreateConnection<LogicValue>();
                var CtoDemux3 = graph.CreateConnection<LogicValue>();
                var Demux3toXor2 = graph.CreateConnection<LogicValue>();
                var Xor1toDemux4 = graph.CreateConnection<LogicValue>();
                var Demux4toXor2 = graph.CreateConnection<LogicValue>();
                var Demux4toAnd1 = graph.CreateConnection<LogicValue>();
                var Demux3toAnd1 = graph.CreateConnection<LogicValue>();
                var Demux1toAnd2 = graph.CreateConnection<LogicValue>();
                var Demux2toAnd2 = graph.CreateConnection<LogicValue>();
                var And1toOr = graph.CreateConnection<LogicValue>();
                var And2toOr = graph.CreateConnection<LogicValue>();

                var toCarryOut = graph.CreateConnection<LogicValue>();
                var toSum = graph.CreateConnection<LogicValue>();

                // A
                graph.AddNode(new PulseValue<LogicValue>(a))
                    .SetOutput("OUT", AtoDemux1);

                // Demux1
                graph.AddNode(new Splitter<LogicValue>())
                    .SetInput("IN", AtoDemux1)
                    .AddOutput(Demux1toXor1)
                    .AddOutput(Demux1toAnd2);

                // B
                graph.AddNode(new PulseValue<LogicValue>(b))
                    .SetOutput("OUT", BtoDemux2);

                // Demux2
                graph.AddNode(new Splitter<LogicValue>())
                    .SetInput("IN", BtoDemux2)
                    .AddOutput(Demux2toXor1)
                    .AddOutput(Demux2toAnd2);

                // C
                graph.AddNode(new PulseValue<LogicValue>(c))
                    .SetOutput("OUT", CtoDemux3);

                // Demux3
                graph.AddNode(new Splitter<LogicValue>())
                    .SetInput("IN", CtoDemux3)
                    .AddOutput(Demux3toXor2)
                    .AddOutput(Demux3toAnd1);

                // Xor1
                graph.AddNode(new XorGate())
                    .SetInput("INA", Demux1toXor1)
                    .SetInput("INB", Demux2toXor1)
                    .SetOutput("OUT", Xor1toDemux4);

                // Xor2
                graph.AddNode(new XorGate())
                    .SetInput("INA", Demux3toXor2)
                    .SetInput("INB", Demux4toXor2)
                    .SetOutput("OUT", toSum);

                var sumCell = graph.AddNode(new Register<LogicValue>())
                    .SetInput("IN", toSum);

                // Demux4
                graph.AddNode(new Splitter<LogicValue>())
                    .SetInput("IN", Xor1toDemux4)
                    .AddOutput(Demux4toXor2)
                    .AddOutput(Demux4toAnd1);

                // And1
                graph.AddNode(new AndGate())
                    .SetInput("INA", Demux4toAnd1)
                    .SetInput("INB", Demux3toAnd1)
                    .SetOutput("OUT", And1toOr);

                // And2
                graph.AddNode(new AndGate())
                    .SetInput("INB", Demux2toAnd2)
                    .SetInput("INA", Demux1toAnd2)
                    .SetOutput("OUT", And2toOr);

                // Or
                graph.AddNode(new OrGate())
                    .SetInput("INB", And1toOr)
                    .SetInput("INA", And2toOr)
                    .SetOutput("OUT", toCarryOut); // OUT

                // Printer
                var carryCell = graph.AddNode(new Register<LogicValue>())
                    .SetInput("IN", toCarryOut);

                await graph.EvaluateAsync();

                sum?.Send(sumCell.GetValue());
                carryOut?.Send(carryCell.GetValue());
            }

            sum?.Close();
            carryOut?.Close();
        }
    }
}
