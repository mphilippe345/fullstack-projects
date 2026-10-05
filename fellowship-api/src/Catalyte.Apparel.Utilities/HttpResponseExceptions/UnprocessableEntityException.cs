using System;

namespace Catalyte.Apparel.Utilities.HttpResponseExceptions
{
    /// <summary>
    /// A custom exception for resource not found errors.
    /// </summary>
    [Serializable]
    public class UnprocessableEntity : Exception, IHttpResponseException
    {
        public UnprocessableEntity(string message)
        {
            Value = new(status: 422, error: "Unprocessable Entity", message: message);
        }
        public HttpResponseExceptionValue Value { get; set; }
    }
}