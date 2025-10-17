using Hypnode.Core;

namespace Hypnode.Logic.Utils
{
    public class ByteSplitterOut : INode
    {
        private readonly Connection<LogicValue>[] inputPorts;
        private Connection<byte>? outputPort = null;

        public ByteSplitterOut()
        {
            inputPorts = new Connection<LogicValue>[8];
        }

        public ByteSplitterOut SetInput(int index, Connection<LogicValue> connection)
        {
            if (index >= 0 && index < 8)
                inputPorts[index] = connection;

            return this;
        }

        public ByteSplitterOut SetOutput(string portName, Connection<byte> connection)
        {
            if (portName == "OUT") outputPort = connection;
            return this;
        }

        public async Task ExecuteAsync()
        {
            bool allReceived;
            LogicValue[] receivedValues = new LogicValue[8];

            do
            {
                allReceived = true;

                for (int i = 0; i < 8; i++)
                {
                    if (inputPorts[i] is null)
                    {
                        throw new InvalidOperationException($"Input port {i} is not set");
                    }

                    if (inputPorts[i]!.TryReceive(out var value))
                    {
                        receivedValues[i] = value;
                    }
                    else
                    {
                        allReceived = false;
                        break;
                    }
                }

                if (allReceived)
                {
                    byte outputByte = (byte)Enumerable.Range(0, 8)
                        .Select(i => (receivedValues[i] == LogicValue.True) ? (1 << i) : 0)
                        .Aggregate(0, (current, bitValue) => current | bitValue);

                    outputPort?.Send(outputByte);
                }
            } while (allReceived);

            outputPort?.Close();
        }
    }
}
