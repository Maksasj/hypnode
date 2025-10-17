using Hypnode.Async;
using Hypnode.Logic;
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
                .SetOutput("OUT", conn1);

            graph.AddNode(new Squarer())
                .SetInput("IN", conn1)
                .SetOutput("OUT", conn2);

            graph.AddNode(new Printer<int>())
                .SetInput("IN", conn2);

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
                .SetOutput("OUT", AtoDemux1);

            // Demux1
            graph.AddNode(new Demux<LogicValue>())
                .SetInput("IN", AtoDemux1)
                .AddOutput(Demux1toXor1)
                .AddOutput(Demux1toAnd2);

            // B
            graph.AddNode(new PulseValue<LogicValue>(LogicValue.True))
                .SetOutput("OUT", BtoDemux2);

            // Demux2
            graph.AddNode(new Demux<LogicValue>())
                .SetInput("IN", BtoDemux2)
                .AddOutput(Demux2toXor1)
                .AddOutput(Demux2toAnd2);

            // C
            graph.AddNode(new PulseValue<LogicValue>(LogicValue.True))
                .SetOutput("OUT", CtoDemux3);

            // Demux3
            graph.AddNode(new Demux<LogicValue>())
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
            graph.AddNode(new Demux<LogicValue>())
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
            graph.AddNode(new Printer<LogicValue>())
                .SetInput("IN", toCarryOut);

            await graph.EvaluateAsync();
        }

        private static async Task TestSome()
        {
            var graph = new AsyncNodeGraph();
            var connection = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<LogicValue>(LogicValue.False))
                .SetOutput("OUT", connection);

            var result = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", connection);

            await graph.EvaluateAsync();
        }

        public static async Task Main()
        {
            await Adder();
        }
    }
}