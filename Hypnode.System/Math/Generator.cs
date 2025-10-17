using Hypnode.Core;

namespace Hypnode.System.Math
{
    public class Generator : INode
    {
        private Connection<int>? outputPort = null;

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "OUT" && connection is Connection<int> con1) outputPort = con1;

            return this;
        }

        public async Task ExecuteAsync()
        {
            var i = 0;

            while (true)
                outputPort?.Send(i++);
        }
    }
}
