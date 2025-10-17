namespace Hypnode.Core
{
    public abstract class IConnection<T> : ICloseableConnection
    {
        public abstract T Receive();

        public abstract void Send(T packet);

        public abstract bool TryReceive(out T packet);

        public abstract void Close();
    }
}
