using Hypnode.Core;

namespace Hypnode.System.IO
{
    public class Printer<T> : INode
    {
        private Connection<T>? inputPort = null;

        public void SetInput(string portName, Connection<T> connection)
        {
            if (portName == "IN") inputPort = connection;
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
