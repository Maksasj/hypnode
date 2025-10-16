using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.System
{
    public class SystemTests
    {
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
    }
}
