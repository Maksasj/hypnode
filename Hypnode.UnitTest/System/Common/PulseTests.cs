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
                .SetPort("OUT", connection);

            var result = new Register<LogicValue>();
            graph.AddNode(result).SetPort("IN", connection);

            await graph.EvaluateAsync();

            Assert.That(result.GetValue(), Is.EqualTo(value));
        }
    }
}
