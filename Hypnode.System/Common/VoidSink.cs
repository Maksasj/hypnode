using Hypnode.Core;

namespace Hypnode.System.Common
{
    public class VoidSink<T> : INode
    {
        private List<IConnection<T>> inputPorts;

        public VoidSink()
        {
            inputPorts = [];
        }

        public VoidSink<T> AddInput(IConnection<T> connection)
        {
            inputPorts.Add(connection);
            return this;
        }

        public async Task ExecuteAsync()
        {
            if (inputPorts.Count == 0)
                return;

            var consumptionTasks = new List<Task>();

            foreach (var connection in inputPorts)
                consumptionTasks.Add(Task.Run(() => VoidSink<T>.ConsumeConnection(connection)));

            await Task.WhenAll(consumptionTasks);
        }

        private static void ConsumeConnection(IConnection<T> connection)
        {
            while (connection.TryReceive(out _))
            {

            }
        }
    }
}
