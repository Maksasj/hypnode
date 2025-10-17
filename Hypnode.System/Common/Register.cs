using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class Register<T> : INode
    {
        private T? value;
        private Connection<T>? inputPort = null;

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "IN" && connection is Connection<T> con) inputPort = con;

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
