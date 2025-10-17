using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class Register<T> : INode
    {
        private T? value;
        private IConnection<T>? inputPort = null;

        public Register<T> SetInput(string portName, IConnection<T> connection)
        {
            if (portName == "IN") inputPort = connection;
            return this;
        }

        public T? GetValue() => value;

        public async Task ExecuteAsync()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port is not set");

            while (inputPort.TryReceive(out var packet))
            {
                value = packet;
            }
        }
    }
}
