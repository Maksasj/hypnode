namespace Hypnode.Core
{
    public abstract class Connection<T> : IConnection
    {
        public abstract T Receive();

        public abstract void Send(T packet);

        public abstract bool TryReceive(out T packet);

        public abstract void Close();
    }
}
