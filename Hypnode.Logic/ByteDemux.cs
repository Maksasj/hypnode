using Hypnode.Core;

namespace Hypnode.Logic
{
    public class ByteDemux : INode
    {
        private IConnection<byte>? inputPort = null;
        private readonly IConnection<LogicValue>[] outputPorts;

        public ByteDemux()
        {
            outputPorts = [];
            outputPorts = new IConnection<LogicValue>[8];
        }

        public ByteDemux SetInput(string portName, IConnection<byte> connection)
        {
            if (portName == "IN") inputPort = connection;
            return this;
        }

        public ByteDemux SetOutput(int index, IConnection<LogicValue> connection)
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

                int portIndex = 0;
                foreach (var connection in outputPorts)
                {
                    connection?.Send(values[portIndex]);
                }
            }

            foreach (var connection in outputPorts)
                connection?.Close();
        }
    }
}
