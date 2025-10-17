using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.System.Common
{
    public class PulseTests
    {
        [TestCase(LogicValue.False)]
        [TestCase(LogicValue.True)]
        public async Task TestPulse_CorrectValue(LogicValue value)
        {
            var graph = new AsyncNodeGraph();
            var connection = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<LogicValue>(value))
                .SetOutput("OUT", connection);

            var result = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", connection);

            await graph.EvaluateAsync();

            Assert.That(result.GetValue(), Is.EqualTo(value));
        }
    }
}
