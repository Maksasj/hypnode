using Hypnode.Core;

namespace Hypnode.System.Math
{
    public class Generator : INode
    {
        private Connection<int>? outputPort = null;

        public void SetOutput(string portName, Connection<int> connection)
        {
            if (portName == "OUT") outputPort = connection;
        }

        public async Task ExecuteAsync()
        {
            var i = 0;

            while (true)
                outputPort?.Send(i++);
        }
    }
}
