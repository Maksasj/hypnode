using Hypnode.Async;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.System.Common
{
    public class VoidTests
    {
        [Test]
        public async Task TestVoid_SingleConnection()
        {
            var graph = new AsyncNodeGraph();
            var connection = graph.CreateConnection<byte>();

            graph.AddNode(new PulseValue<byte>(1))
                .SetOutput("OUT", connection);

            var result = graph.AddNode(new VoidSink<byte>())
                .AddInput(connection);

            await graph.EvaluateAsync();

            Assert.Pass();
        }

        [Test]
        public async Task TestVoid_MultipleConnections()
        {
            var graph = new AsyncNodeGraph();
            var connection1 = graph.CreateConnection<byte>();
            var connection2 = graph.CreateConnection<byte>();

            graph.AddNode(new PulseValue<byte>(1))
                .SetOutput("OUT", connection1);

            graph.AddNode(new PulseValue<byte>(1))
                .SetOutput("OUT", connection2);

            graph.AddNode(new VoidSink<byte>())
                .AddInput(connection1)
                .AddInput(connection2);

            await graph.EvaluateAsync();

            Assert.Pass();
        }
    }
}
