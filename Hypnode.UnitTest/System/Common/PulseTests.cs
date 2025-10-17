using Hypnode.Async;
using Hypnode.Core;
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

            var pulse = graph.AddNode(new PulseValue<LogicValue>(value));
            var result = graph.AddNode(new Register<LogicValue>());

            graph.AddConnection<LogicValue>(pulse, "OUT", result, "IN");

            await graph.EvaluateAsync();

            Assert.That(result.GetValue(), Is.EqualTo(value));
        }
    }
}
