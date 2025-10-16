using Hypnode.Core;

namespace Hypnode.System
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

        public void Execute()
        {
            while (true)
                outputPort?.Send(Value);
        }
    }
}
