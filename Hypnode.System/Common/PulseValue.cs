using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class PulseValue<T> : INode
    {
        private T Value { get; set; }
        private IConnection<T>? outputPort = null;

        public PulseValue(T value)
        {
            this.Value = value;
        }

        public void SetOutput(string portName, IConnection<T> connection)
        {
            if (portName == "OUT") outputPort = connection;
        }

        public void Execute()
        {
            outputPort?.Send(Value);
        }
    }
}
