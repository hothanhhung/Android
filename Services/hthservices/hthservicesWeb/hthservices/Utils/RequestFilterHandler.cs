using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Web;

namespace hthservices.Utils
{
    public class RequestFilterHandler : DelegatingHandler
    {
        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            if (DenyInfo.IsDenyUserRequest(request))
            {
                return request.CreateResponse(System.Net.HttpStatusCode.OK, DenyInfo.DenyResponseMessage);
            }
            return await base.SendAsync(request, cancellationToken);
        }
    }
}