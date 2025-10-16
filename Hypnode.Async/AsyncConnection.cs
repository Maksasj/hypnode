using Hypnode.Core;
using System.Collections.Concurrent;

namespace Hypnode.Async
{
    public class AsyncConnection<T> : IConnection<T>
    {
        public BlockingCollection<T> Buffer { get; } = new BlockingCollection<T>(1);

        public T Receive() => Buffer.Take();

        public void Send(T packet) => Buffer.Add(packet);
    }
}
