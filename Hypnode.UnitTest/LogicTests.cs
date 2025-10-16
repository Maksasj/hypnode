using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.System;

namespace Hypnode.UnitTests
{
    public class LogicTests
    {
        [TestCase(LogicValue.False, LogicValue.False, LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.True, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.True, LogicValue.False)]
        public async Task TestXor(LogicValue a, LogicValue b, LogicValue expect)
        {
            var graph = new AsyncNodeGraph();
            var connection1 = graph.CreateConnection<LogicValue>();
            var connection2 = graph.CreateConnection<LogicValue>();
            var connection3 = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<LogicValue>(a))
                .SetOutput("OUT", connection1);

            graph.AddNode(new PulseValue<LogicValue>(b))
                .SetOutput("OUT", connection2);

            graph.AddNode(new Xor())
                .SetInput("INA", connection1)
                .SetInput("INB", connection2)
                .SetOutput("OUT", connection3);

            var result = graph.AddNode(new Cell<LogicValue>())
                .SetInput("IN", connection3);

            await graph.EvaluateAsync(TimeSpan.FromSeconds(0.2));

            Assert.That(expect, Is.EqualTo(result.GetValue()));
        }

        [TestCase(LogicValue.False, LogicValue.False, LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.True, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.True, LogicValue.True)]
        public async Task TestOr(LogicValue a, LogicValue b, LogicValue expect)
        {
            var graph = new AsyncNodeGraph();
            var connection1 = graph.CreateConnection<LogicValue>();
            var connection2 = graph.CreateConnection<LogicValue>();
            var connection3 = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<LogicValue>(a))
                .SetOutput("OUT", connection1);

            graph.AddNode(new PulseValue<LogicValue>(b))
                .SetOutput("OUT", connection2);

            graph.AddNode(new Or())
                .SetInput("INA", connection1)
                .SetInput("INB", connection2)
                .SetOutput("OUT", connection3);

            var result = graph.AddNode(new Cell<LogicValue>())
                .SetInput("IN", connection3);

            await graph.EvaluateAsync(TimeSpan.FromSeconds(0.2));

            Assert.That(expect, Is.EqualTo(result.GetValue()));
        }

        [TestCase(LogicValue.False, LogicValue.False, LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.True, LogicValue.False)]
        [TestCase(LogicValue.True, LogicValue.False, LogicValue.False)]
        [TestCase(LogicValue.True, LogicValue.True, LogicValue.True)]
        public async Task TestAnd(LogicValue a, LogicValue b, LogicValue expect)
        {
            var graph = new AsyncNodeGraph();
            var connection1 = graph.CreateConnection<LogicValue>();
            var connection2 = graph.CreateConnection<LogicValue>();
            var connection3 = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<LogicValue>(a))
                .SetOutput("OUT", connection1);

            graph.AddNode(new PulseValue<LogicValue>(b))
                .SetOutput("OUT", connection2);

            graph.AddNode(new And())
                .SetInput("INA", connection1)
                .SetInput("INB", connection2)
                .SetOutput("OUT", connection3);

            var result = graph.AddNode(new Cell<LogicValue>())
                .SetInput("IN", connection3);

            await graph.EvaluateAsync(TimeSpan.FromSeconds(0.2));

            Assert.That(result.GetValue(), Is.EqualTo(expect));
        }

        [TestCase(LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.False)]
        public async Task TestNot(LogicValue value, LogicValue expect)
        {
            var graph = new AsyncNodeGraph();
            var connection1 = graph.CreateConnection<LogicValue>();
            var connection2 = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<LogicValue>(value))
                .SetOutput("OUT", connection1);

            graph.AddNode(new Not())
                .SetInput("IN", connection1)
                .SetOutput("OUT", connection2);

            var result = graph.AddNode(new Cell<LogicValue>())
                .SetInput("IN", connection2);

            await graph.EvaluateAsync(TimeSpan.FromSeconds(0.2));

            Assert.That(result.GetValue(), Is.EqualTo(expect));
        }

        [TestCase(LogicValue.False)]
        [TestCase(LogicValue.True)]
        public async Task TestPulse(LogicValue value)
        {
            var graph = new AsyncNodeGraph();
            var connection = graph.CreateConnection<LogicValue>();
            
            graph.AddNode(new PulseValue<LogicValue>(value))
                .SetOutput("OUT", connection);

            var result = graph.AddNode(new Cell<LogicValue>())
                .SetInput("IN", connection);

            await graph.EvaluateAsync(TimeSpan.FromSeconds(0.2));

            Assert.That(result.GetValue(), Is.EqualTo(value));
        }

        [TestCase(LogicValue.False,LogicValue.False,LogicValue.False,LogicValue.False,LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.False, LogicValue.True, LogicValue.True, LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.True, LogicValue.True, LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.False, LogicValue.False, LogicValue.True, LogicValue.False)]
        [TestCase(LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.True, LogicValue.False, LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True)]
        public async Task TestAdder(LogicValue a, LogicValue b, LogicValue cIn, LogicValue sum, LogicValue cOut)
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
            graph.AddNode(new Demux<LogicValue>())
                .SetInput("IN", AtoDemux1)
                .AddOutput(Demux1toXor1)
                .AddOutput(Demux1toAnd2);

            // B
            graph.AddNode(new PulseValue<LogicValue>(b))
                .SetOutput("OUT", BtoDemux2);

            // Demux2
            graph.AddNode(new Demux<LogicValue>())
                .SetInput("IN", BtoDemux2)
                .AddOutput(Demux2toXor1)
                .AddOutput(Demux2toAnd2);

            // C
            graph.AddNode(new PulseValue<LogicValue>(cIn))
                .SetOutput("OUT", CtoDemux3);

            // Demux3
            graph.AddNode(new Demux<LogicValue>())
                .SetInput("IN", CtoDemux3)
                .AddOutput(Demux3toXor2)
                .AddOutput(Demux3toAnd1);

            // Xor1
            graph.AddNode(new Xor())
                .SetInput("INA", Demux1toXor1)
                .SetInput("INB", Demux2toXor1)
                .SetOutput("OUT", Xor1toDemux4);

            // Xor2
            graph.AddNode(new Xor())
                .SetInput("INA", Demux3toXor2)
                .SetInput("INB", Demux4toXor2)
                .SetOutput("OUT", toSum);

            var sumCell = graph.AddNode(new Cell<LogicValue>())
                .SetInput("IN", toSum);

            // Demux4
            graph.AddNode(new Demux<LogicValue>())
                .SetInput("IN", Xor1toDemux4)
                .AddOutput(Demux4toXor2)
                .AddOutput(Demux4toAnd1);

            // And1
            graph.AddNode(new And())
                .SetInput("INA", Demux4toAnd1)
                .SetInput("INB", Demux3toAnd1)
                .SetOutput("OUT", And1toOr);

            // And2
            graph.AddNode(new And())
                .SetInput("INB", Demux2toAnd2)
                .SetInput("INA", Demux1toAnd2)
                .SetOutput("OUT", And2toOr);

            // Or
            graph.AddNode(new Or())
                .SetInput("INB", And1toOr)
                .SetInput("INA", And2toOr)
                .SetOutput("OUT", toCarryOut); // OUT

            // Printer
            var carry = graph.AddNode(new Cell<LogicValue>())
                .SetInput("IN", toCarryOut);

            await graph.EvaluateAsync(TimeSpan.FromSeconds(0.2));

            Assert.That(sumCell.GetValue(), Is.EqualTo(sum));
            // Assert.That(cOut, Is.EqualTo(carry.GetValue()));
        }
    }
}
