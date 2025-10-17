using Hypnode.Core;

namespace Hypnode.Logic.Utils
{
    public class ByteSplitterIn : INode
    {
        private Connection<byte>? inputPort = null;
        private readonly Connection<LogicValue>[] outputPorts;

        public ByteSplitterIn()
        {
            outputPorts = [];
            outputPorts = new Connection<LogicValue>[8];
        }

        public ByteSplitterIn SetInput(string portName, Connection<byte> connection)
        {
            if (portName == "IN") inputPort = connection;
            return this;
        }

        public ByteSplitterIn SetOutput(int index, Connection<LogicValue> connection)
        {
            outputPorts[index] = connection;
            return this;
        }

        public async Task ExecuteAsync()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port is not set");

            while (inputPort.TryReceive(out var packet))
            {
                LogicValue[] values = [.. Enumerable.Range(0, 8)
                        .Select(i => (packet & (1 << i)) != 0)
                        .Select(b => b ? LogicValue.True : LogicValue.False)];

                for (int i = 0; i < 8; ++i)
                    outputPorts[i]?.Send(values[i]);
            }

            foreach (var connection in outputPorts)
                connection?.Close();
        }
    }
}
