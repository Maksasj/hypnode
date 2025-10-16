using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.Logic
{
    public class GatesTests
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

            graph.AddNode(new XorGate())
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

            graph.AddNode(new OrGate())
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

            graph.AddNode(new AndGate())
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

            graph.AddNode(new NotGate())
                .SetInput("IN", connection1)
                .SetOutput("OUT", connection2);

            var result = graph.AddNode(new Cell<LogicValue>())
                .SetInput("IN", connection2);

            await graph.EvaluateAsync(TimeSpan.FromSeconds(0.2));

            Assert.That(result.GetValue(), Is.EqualTo(expect));
        }
    }
}
