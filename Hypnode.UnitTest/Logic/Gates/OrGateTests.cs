using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.Logic.Gates;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.Logic.Gates
{
    public class OrGateTests
    {
        [TestCase(LogicValue.False, LogicValue.False, LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.True, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.True, LogicValue.True)]
        public async Task TestOr_CorrectValue(LogicValue a, LogicValue b, LogicValue expect)
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

            var result = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", connection3);

            await graph.EvaluateAsync();

            Assert.That(expect, Is.EqualTo(result.GetValue()));
        }
    }
}
