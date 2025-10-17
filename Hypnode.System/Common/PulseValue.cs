using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class PulseValue<T> : INode
    {
        private T Value { get; set; }

        private Connection<T>? outputPort = null;

        public PulseValue(T value)
        {
            Value = value;
        }

        public INode SetPort(string portName, IConnection connection)
        {
            if (portName == "OUT" && connection is Connection<T> con) outputPort = con;
            return this;
        }

        public async Task ExecuteAsync()
        {
            outputPort?.Send(Value);
            outputPort?.Close();
        }
    }
}
