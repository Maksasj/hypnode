using Hypnode.Core;

namespace Hypnode.System.IO
{
    public class Printer<T> : INode
    {
        private Connection<T>? inputPort = null;

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "IN" && connection is Connection<T> conn) inputPort = conn;

            return this;
        }

        public async Task ExecuteAsync()
        {
            if (inputPort is null)
                throw new InvalidOperationException("Input port is not set");

            while (true)
            {
                var packet = inputPort.Receive();
                Console.WriteLine($"{packet}");
            }
        }
    }
}
