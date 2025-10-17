using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.Logic.Utils;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.Logic.Utils
{
    public class ByteDemuxTests
    {

        [TestCase(0b00000000, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False)]
        [TestCase(0b10000000, LogicValue.True, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False)]
        [TestCase(0b11111111, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True)]
        [TestCase(0b01010101, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True)]
        [TestCase(0b10101010, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False)]
        public async Task TestByteDemux_CorrectValues(byte value, LogicValue b7e, LogicValue b6e, LogicValue b5e, LogicValue b4e, LogicValue b3e, LogicValue b2e, LogicValue b1e, LogicValue b0e)
        {
            var graph = new AsyncNodeGraph();
            var input = graph.CreateConnection<byte>();

            var b0c = graph.CreateConnection<LogicValue>();
            var b1c = graph.CreateConnection<LogicValue>();
            var b2c = graph.CreateConnection<LogicValue>();
            var b3c = graph.CreateConnection<LogicValue>();
            var b4c = graph.CreateConnection<LogicValue>();
            var b5c = graph.CreateConnection<LogicValue>();
            var b6c = graph.CreateConnection<LogicValue>();
            var b7c = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<byte>(value))
                .SetOutput("OUT", input);

            graph.AddNode(new ByteDemux())
                .SetInput("IN", input)
                .SetOutput(0, b0c)
                .SetOutput(1, b1c)
                .SetOutput(2, b2c)
                .SetOutput(3, b3c)
                .SetOutput(4, b4c)
                .SetOutput(5, b5c)
                .SetOutput(6, b6c)
                .SetOutput(7, b7c);

            var b0 = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", b0c);

            var b1 = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", b1c);

            var b2 = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", b2c);

            var b3 = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", b3c);

            var b4 = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", b4c);

            var b5 = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", b5c);

            var b6 = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", b6c);

            var b7 = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", b7c);

            await graph.EvaluateAsync();

            Assert.That(b0.GetValue(), Is.EqualTo(b0e));
            Assert.That(b1.GetValue(), Is.EqualTo(b1e));
            Assert.That(b2.GetValue(), Is.EqualTo(b2e));
            Assert.That(b3.GetValue(), Is.EqualTo(b3e));
            Assert.That(b4.GetValue(), Is.EqualTo(b4e));
            Assert.That(b5.GetValue(), Is.EqualTo(b5e));
            Assert.That(b6.GetValue(), Is.EqualTo(b6e));
            Assert.That(b7.GetValue(), Is.EqualTo(b7e));
        }
    }
}
