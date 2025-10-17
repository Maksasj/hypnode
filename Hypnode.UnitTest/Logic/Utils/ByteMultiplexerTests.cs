using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.Logic.Utils;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.Logic.Utils
{
    public class ByteMultiplexerTests
    {

        [TestCase(0b00000000, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False)]
        [TestCase(0b10000000, LogicValue.True, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False)]
        [TestCase(0b11111111, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True)]
        [TestCase(0b01010101, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True)]
        [TestCase(0b10101010, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False)]
        public async Task TestByteMultiplexer_CorrectValues(byte expected, LogicValue b7, LogicValue b6, LogicValue b5, LogicValue b4, LogicValue b3, LogicValue b2, LogicValue b1, LogicValue b0)
        {
            var graph = new AsyncNodeGraph();

            var b0c = graph.CreateConnection<LogicValue>();
            var b1c = graph.CreateConnection<LogicValue>();
            var b2c = graph.CreateConnection<LogicValue>();
            var b3c = graph.CreateConnection<LogicValue>();
            var b4c = graph.CreateConnection<LogicValue>();
            var b5c = graph.CreateConnection<LogicValue>();
            var b6c = graph.CreateConnection<LogicValue>();
            var b7c = graph.CreateConnection<LogicValue>();

            var output = graph.CreateConnection<byte>();

            graph.AddNode(new PulseValue<LogicValue>(b0)).SetOutput("OUT", b0c);
            graph.AddNode(new PulseValue<LogicValue>(b1)).SetOutput("OUT", b1c);
            graph.AddNode(new PulseValue<LogicValue>(b2)).SetOutput("OUT", b2c);
            graph.AddNode(new PulseValue<LogicValue>(b3)).SetOutput("OUT", b3c);
            graph.AddNode(new PulseValue<LogicValue>(b4)).SetOutput("OUT", b4c);
            graph.AddNode(new PulseValue<LogicValue>(b5)).SetOutput("OUT", b5c);
            graph.AddNode(new PulseValue<LogicValue>(b6)).SetOutput("OUT", b6c);
            graph.AddNode(new PulseValue<LogicValue>(b7)).SetOutput("OUT", b7c);

            graph.AddNode(new ByteSplitterOut())
                .SetInput(0, b0c)
                .SetInput(1, b1c)
                .SetInput(2, b2c)
                .SetInput(3, b3c)
                .SetInput(4, b4c)
                .SetInput(5, b5c)
                .SetInput(6, b6c)
                .SetInput(7, b7c)
                .SetOutput("OUT", output);

            var result = graph.AddNode(new Register<byte>())
                .SetInput("IN", output);

            await graph.EvaluateAsync();

            Assert.That(result.GetValue(), Is.EqualTo(expected));
        }
    }
}
