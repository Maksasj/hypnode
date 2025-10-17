using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class PulseValue<T> : INode
    {
        private T Value { get; set; }
        private Connection<T>? outputPort = null;

        public PulseValue(T value)
        {
            this.Value = value;
        }

        public void SetOutput(string portName, Connection<T> connection)
        {
            if (portName == "OUT") outputPort = connection;
        }

        public async Task ExecuteAsync()
        {
            outputPort?.Send(Value);
            outputPort?.Close();
        }
    }
}
