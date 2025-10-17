using Hypnode.Async;
using Hypnode.Logic.Compound;
using Hypnode.System.Common;

namespace Hypnode.UnitTests.Logic.Compound
{
    public class FullAdderByteTests
    {
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

            graph.AddNode(new FullAdderByte())
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
