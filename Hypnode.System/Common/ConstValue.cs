using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class ConstValue<T> : INode
    {
        private T Value { get; set; }
        private IConnection<T>? outputPort = null;

        public ConstValue(T value)
        {
            this.Value = value;
        }

        public void SetOutput(string portName, IConnection<T> connection)
        {
            if (portName == "OUT") outputPort = connection;
        }

        public async Task ExecuteAsync()
        {
            while (true)
                outputPort?.Send(Value);
        }
    }
}
