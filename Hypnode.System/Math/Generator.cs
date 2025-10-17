using Hypnode.Core;

namespace Hypnode.System.Math
{
    public class Generator : INode
    {
        private IConnection<int>? outputPort = null;

        public void SetOutput(string portName, IConnection<int> connection)
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
