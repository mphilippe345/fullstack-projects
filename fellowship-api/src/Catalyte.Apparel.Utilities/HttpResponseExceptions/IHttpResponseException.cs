namespace Catalyte.Apparel.Utilities.HttpResponseExceptions
{
    public interface IHttpResponseException
    {
        public HttpResponseExceptionValue Value { get; set; }
    }
}
