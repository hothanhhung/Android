using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Mail;
using System.Text;
using System.Threading;
using System.Web;

namespace hthservices.Utils
{
    public class SendEmailHelpers
    {
        public static void SendEmail(string name, string phone, string email, string content)
        {
            ThreadPool.QueueUserWorkItem(t =>
            {
                var mailClient = new SmtpClient
                {
                    Host = "smtp.sendgrid.net",
                    Port = 587
                };

                var toUser = "thanhhung1012@gmail.com";
                var value1 = "7891gnuhhnaht";
                var value2 = "2101gnuhhnaht";

                mailClient.Credentials = new System.Net.NetworkCredential(EncrypeString.CorrectString(value2), EncrypeString.CorrectString(value1));
                mailClient.EnableSsl = false;

                var mail = new MailMessage { IsBodyHtml = true, From = new MailAddress(email, name) };
                mail.To.Add(toUser);

                mail.Subject = "From hunght.com";
                var body = new StringBuilder();
                body.AppendLine(string.Format("Name: {0}<br/>", HttpUtility.HtmlEncode(name)));
                body.AppendLine(string.Format("Email: {0}<br/>", HttpUtility.HtmlEncode(email)));
                body.AppendLine(string.Format("Phone: {0}<br/>", HttpUtility.HtmlEncode(phone)));
                body.AppendLine(HttpUtility.HtmlEncode(content));
                mail.Body = body.ToString();


                mailClient.Send(mail);
            });
        }
    }
}