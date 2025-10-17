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

            graph.AddNode(new PulseValue<LogicValue>(b0)).SetPort("OUT", b0c);
            graph.AddNode(new PulseValue<LogicValue>(b1)).SetPort("OUT", b1c);
            graph.AddNode(new PulseValue<LogicValue>(b2)).SetPort("OUT", b2c);
            graph.AddNode(new PulseValue<LogicValue>(b3)).SetPort("OUT", b3c);
            graph.AddNode(new PulseValue<LogicValue>(b4)).SetPort("OUT", b4c);
            graph.AddNode(new PulseValue<LogicValue>(b5)).SetPort("OUT", b5c);
            graph.AddNode(new PulseValue<LogicValue>(b6)).SetPort("OUT", b6c);
            graph.AddNode(new PulseValue<LogicValue>(b7)).SetPort("OUT", b7c);

            graph.AddNode(new ByteSplitterOut())
                .SetPort("0".ToString(), b0c)
                .SetPort("1".ToString(), b1c)
                .SetPort("2".ToString(), b2c)
                .SetPort("3".ToString(), b3c)
                .SetPort("4".ToString(), b4c)
                .SetPort("5".ToString(), b5c)
                .SetPort("6".ToString(), b6c)
                .SetPort("7".ToString(), b7c)
                .SetPort("OUT", output);

            var result = new Register<byte>();
            graph.AddNode(result).SetPort("IN", output);

            await graph.EvaluateAsync();

            Assert.That(result.GetValue(), Is.EqualTo(expected));
        }
    }
}
