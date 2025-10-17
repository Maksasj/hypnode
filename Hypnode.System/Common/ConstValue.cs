using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class ConstValue<T> : INode
    {
        private T Value { get; set; }
        private Connection<T>? outputPort = null;

        public ConstValue(T value)
        {
            this.Value = value;
        }

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "OUT" && connection is Connection<T> con) outputPort = con;
            return this;
        }

        public async Task ExecuteAsync()
        {
            while (true)
                outputPort?.Send(Value);
        }
    }
}
