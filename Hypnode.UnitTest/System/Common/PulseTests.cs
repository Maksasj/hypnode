using Hypnode.Async;
using Hypnode.Core;
using Hypnode.Logic;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.System.Common
{
    public abstract class PulseTests<TGraph> where TGraph : INodeGraph, new()
    {
        [TestCase(LogicValue.False)]
        [TestCase(LogicValue.True)]
        public async Task TestPulse_CorrectValue(LogicValue value)
        {
            var graph = new TGraph();

            var pulse = graph.AddNode(new PulseValue<LogicValue>(value));
            var result = graph.AddNode(new Register<LogicValue>());

            graph.AddConnection<LogicValue>(pulse, "OUT", result, "IN");

            await graph.EvaluateAsync();

            Assert.That(result.GetValue(), Is.EqualTo(value));
        }
    }

    [TestFixture]
    public class AsyncNodeGraph_PulseTests : PulseTests<AsyncNodeGraph>
    {

    }
}
