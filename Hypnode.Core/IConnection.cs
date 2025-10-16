namespace Hypnode.Core
{
    public interface IConnection<T>
    {
        public T Receive();

        public void Send(T packet);
    }
}
