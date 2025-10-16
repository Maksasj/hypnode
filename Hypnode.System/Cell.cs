using Hypnode.Core;

namespace Hypnode.System
{
    public class Cell<T> : INode
    {
        private T? value;
        private IConnection<T>? inputPort = null;

        public Cell<T> SetInput(string portName, IConnection<T> connection)
        {
            if (portName == "IN") inputPort = connection;
            return this;
        }

        public T? GetValue() => value;

        public void Execute()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port is not set");

            while (true)
            {
                var packet = inputPort.Receive();
                value = packet;
            }
        }
    }
}
