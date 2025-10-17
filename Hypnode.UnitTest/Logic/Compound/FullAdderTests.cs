using Hypnode.Async;
using Hypnode.Logic;
using Hypnode.Logic.Compound;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.Logic.Compound
{
    public class FullAdderTests
    {
        [TestCase(LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False, LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.False, LogicValue.True, LogicValue.True, LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False)]
        [TestCase(LogicValue.False, LogicValue.True, LogicValue.True, LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.False, LogicValue.False, LogicValue.True, LogicValue.False)]
        [TestCase(LogicValue.True, LogicValue.False, LogicValue.True, LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.True, LogicValue.False, LogicValue.False, LogicValue.True)]
        [TestCase(LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True, LogicValue.True)]
        public async Task TestAdderCompound_CorrectValues(LogicValue a, LogicValue b, LogicValue cIn, LogicValue sum, LogicValue cOut)
        {
            var graph = new AsyncNodeGraph();
            var ain = graph.CreateConnection<LogicValue>();
            var bin = graph.CreateConnection<LogicValue>();
            var cin = graph.CreateConnection<LogicValue>();

            var outsum = graph.CreateConnection<LogicValue>();
            var outc = graph.CreateConnection<LogicValue>();

            graph.AddNode(new PulseValue<LogicValue>(a))
                .SetOutput("OUT", ain);

            graph.AddNode(new PulseValue<LogicValue>(b))
               .SetOutput("OUT", bin);

            graph.AddNode(new PulseValue<LogicValue>(cIn))
                .SetOutput("OUT", cin);

            graph.AddNode(new FullAdder(new AsyncNodeGraph()))
                .SetInput("INA", ain)
                .SetInput("INB", bin)
                .SetInput("INC", cin)
                .SetOutput("OUTSUM", outsum)
                .SetOutput("OUTC", outc);

            var sumCell = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", outsum);

            var carryCell = graph.AddNode(new Register<LogicValue>())
                .SetInput("IN", outc);

            await graph.EvaluateAsync();

            Assert.That(sumCell.GetValue(), Is.EqualTo(sum));
            Assert.That(carryCell.GetValue(), Is.EqualTo(cOut));
        }

        [TestCase(0b00000000, 0b00000000)]
        [TestCase(0b00001010, 0b00000000)]
        [TestCase(0b00000000, 0b00001010)]
        [TestCase(0b00000001, 0b00000001)]
        [TestCase(0b00001010, 0b00110010)]
        [TestCase(0b01100100, 0b01100100)]
        [TestCase(0b10000000, 0b01111111)]
        [TestCase(0b11001000, 0b00110111)]
        [TestCase(0b11111010, 0b00000101)]
        [TestCase(0b00000000, 0b11111111)]
        [TestCase(0b11111111, 0b00000000)]
        public async Task TestAdderByteCompound_CorrectValues(byte a, byte b)
        {
            var graph = new AsyncNodeGraph();
            var ain = graph.CreateConnection<byte>();
            var bin = graph.CreateConnection<byte>();

            var outsum = graph.CreateConnection<byte>();

            graph.AddNode(new PulseValue<byte>(a))
                .SetOutput("OUT", ain);

            graph.AddNode(new PulseValue<byte>(b))
               .SetOutput("OUT", bin);

            graph.AddNode(new FullAdderByte(new AsyncNodeGraph()))
                .SetInput("INA", ain)
                .SetInput("INB", bin)
                .SetOutput("OUTSUM", outsum);

            var sumCell = graph.AddNode(new Register<byte>())
                .SetInput("IN", outsum);

            await graph.EvaluateAsync();

            Assert.That(sumCell.GetValue(), Is.EqualTo(a + b));
        }
    }
}
