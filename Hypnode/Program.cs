using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.Logic.Gates;
using Hypnode.System.Common;
using Hypnode.System.IO;
using Hypnode.System.Math;

namespace Hypnode.Example
{
    class Program
    {
        private static async Task TestCircuit()
        {
            var graph = new AsyncNodeGraph();
            var conn1 = graph.CreateConnection<int>();
            var conn2 = graph.CreateConnection<int>();

            graph.AddNode(new Generator())
                .SetPort("OUT", conn1);

            graph.AddNode(new Squarer())
                .SetPort("IN", conn1)
                .SetPort("OUT", conn2);

            graph.AddNode(new Printer<int>())
                .SetPort("IN", conn2);

            await graph.EvaluateAsync();
        }

        private static async Task Adder()
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
            graph.AddNode(new PulseValue<LogicValue>(LogicValue.True))
                .SetPort("OUT", AtoDemux1);

            // Demux1
            graph.AddNode(new Splitter<LogicValue>())
                .SetPort("IN", AtoDemux1)
                .SetPort("OUT", Demux1toXor1)
                .SetPort("OUT", Demux1toAnd2);

            // B
            graph.AddNode(new PulseValue<LogicValue>(LogicValue.True))
                .SetPort("OUT", BtoDemux2);

            // Demux2
            graph.AddNode(new Splitter<LogicValue>())
                .SetPort("IN", BtoDemux2)
                .SetPort("OUT", Demux2toXor1)
                .SetPort("OUT", Demux2toAnd2);

            // C
            graph.AddNode(new PulseValue<LogicValue>(LogicValue.True))
                .SetPort("OUT", CtoDemux3);

            // Demux3
            graph.AddNode(new Splitter<LogicValue>())
                .SetPort("IN", CtoDemux3)
                .SetPort("OUT", Demux3toXor2)
                .SetPort("OUT", Demux3toAnd1);

            // Xor1
            graph.AddNode(new XorGate())
                .SetPort("INA", Demux1toXor1)
                .SetPort("INB", Demux2toXor1)
                .SetPort("OUT", Xor1toDemux4);

            // Xor2
            graph.AddNode(new XorGate())
                .SetPort("INA", Demux3toXor2)
                .SetPort("INB", Demux4toXor2)
                .SetPort("OUT", toSum);

            var sumCell = graph.AddNode(new Register<LogicValue>())
                .SetPort("IN", toSum);

            // Demux4
            graph.AddNode(new Splitter<LogicValue>())
                .SetPort("IN", Xor1toDemux4)
                .SetPort("OUT", Demux4toXor2)
                .SetPort("OUT", Demux4toAnd1);

            // And1
            graph.AddNode(new AndGate())
                .SetPort("INA", Demux4toAnd1)
                .SetPort("INB", Demux3toAnd1)
                .SetPort("OUT", And1toOr);

            // And2
            graph.AddNode(new AndGate())
                .SetPort("INB", Demux2toAnd2)
                .SetPort("INA", Demux1toAnd2)
                .SetPort("OUT", And2toOr);

            // Or
            graph.AddNode(new OrGate())
                .SetPort("INB", And1toOr)
                .SetPort("INA", And2toOr)
                .SetPort("OUT", toCarryOut); // OUT

            // Printer
            graph.AddNode(new Printer<LogicValue>())
                .SetPort("IN", toCarryOut);

            await graph.EvaluateAsync();
        }

        private static async Task TestSome()
        {
            var graph = new AsyncNodeGraph();
            var connection = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<LogicValue>(LogicValue.False))
                .SetPort("OUT", connection);

            var result = graph.AddNode(new Register<LogicValue>())
                .SetPort("IN", connection);

            await graph.EvaluateAsync();
        }

        public static async Task Main()
        {
            await Adder();
        }
    }
}