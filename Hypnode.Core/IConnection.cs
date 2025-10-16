using System.Collections.Concurrent;

namespace Hypnode.Core
{
    public class IConnection<T>
    {
        public BlockingCollection<T> Buffer { get; } = new BlockingCollection<T>(1);

        public T Receive() => Buffer.Take();

        public void Send(T packet) => Buffer.Add(packet);

        public void Close() => Buffer.CompleteAdding();
    }
}
