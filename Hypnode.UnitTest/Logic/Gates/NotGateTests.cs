using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.Logic.Gates;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.Logic.Gates
{
    public class NotGateTests
    {
        [TestCase(LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.False)]
        public async Task TestNot_CorrectValue(LogicValue value, LogicValue expect)
        {
            var graph = new AsyncNodeGraph();
            var connection1 = graph.CreateConnection<LogicValue>();
            var connection2 = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<LogicValue>(value))
                .SetOutput("OUT", connection1);

            graph.AddNode(new NotGate())
                .SetInput("IN", connection1)
                .SetOutput("OUT", connection2);

            var result = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", connection2);

            await graph.EvaluateAsync();

            Assert.That(result.GetValue(), Is.EqualTo(expect));
        }
    }
}
