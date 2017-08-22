using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using TiviOnline.Models;

namespace TiviOnline.Bussiness
{
    public class DataJsonProcess
    {
        static List<Channel> channels;
        static List<StreamServer> streamServers;
        private static string absolutePathToDataFolder;

        private static string GetAbsolutePathToDataFolder()
        {
            if (string.IsNullOrWhiteSpace(absolutePathToDataFolder))
            {
                if (HttpContext.Current == null)
                {
                    if (HttpRuntime.AppDomainAppPath.EndsWith("\\"))
                    {
                        absolutePathToDataFolder = HttpRuntime.AppDomainAppPath + "Data";
                    }
                    else
                    {
                        absolutePathToDataFolder = HttpRuntime.AppDomainAppPath + "\\Data";
                    }
                }
                else
                {
                    absolutePathToDataFolder = HttpContext.Current.Server.MapPath("~/Data");
                }
            }
            return absolutePathToDataFolder;

        }

        public static List<Channel> Channels
        {
            get
            {
                if (channels == null || channels.Count == 0)
                {
                    try
                    {
                        using (StreamReader r = new StreamReader(GetAbsolutePathToDataFolder() + "\\Channels.json"))
                        {
                            string json = r.ReadToEnd();
                            channels = JsonConvert.DeserializeObject<List<Channel>>(json);
                            channels = channels.Where(p => p.IsActive).ToList();
                        }
                    }
                    catch (Exception ex)
                    {
                        channels = new List<Channel>();
                    }
                }
                return channels;
            }
        }
       
        public static List<StreamServer> StreamServers
        {
            get
            {
                if (streamServers == null || streamServers.Count == 0)
                {
                    try
                    {
                        using (StreamReader r = new StreamReader(GetAbsolutePathToDataFolder() + "\\StreamServers.json"))
                        {
                            string json = r.ReadToEnd();
                            streamServers = JsonConvert.DeserializeObject<List<StreamServer>>(json);
                            streamServers = streamServers.Where(p => p.IsActive).ToList();
                        }
                    }
                    catch (Exception ex)
                    {
                        streamServers = new List<StreamServer>();
                    }
                }
                return streamServers;
            }
        }

        public static void ResetJsonData()
        {
            streamServers = null;
            channels = null;
        }

        public static List<Channel> GetHotChannels()
        {
            return Channels.Where(p => p.Group.Any(g => g == 0)).ToList();
        }

        public static List<Channel> GetVTV_VTCChannels()
        {
            return Channels.Where(p => p.Group.Any(g => g == 1)).ToList();
        }

        public static List<Channel> GetSCTV_HTVChannels()
        {
            return Channels.Where(p => p.Group.Any(g => g == 2)).ToList();
        }

        public static List<Channel> GetLocalChannels()
        {
            return Channels.Where(p => p.Group.Any(g => g == 3)).ToList();
        }

        public static List<Channel> GetForeignChannels()
        {
            return Channels.Where(p => p.Group.Any(g => g == 4)).ToList();
        }

        public static List<Channel> GetFootballChannels()
        {
            return Channels.Where(p => p.Group.Any(g => g == 5)).ToList();
        }

        public static List<Channel> GetOthersChannels()
        {
            return Channels.Where(p => p.Group.Any(g => g == 6)).ToList();
        }

        public static List<StreamServer> GetStreamServersOfChannel(string channelId)
        {
            return StreamServers.Where(p => p.ChannelId.Equals(channelId, StringComparison.OrdinalIgnoreCase)).ToList();
        }

        public static StreamServer GetStreamServer(string id)
        {
            if (string.IsNullOrWhiteSpace(id))
            {
                return null;
            }
            return StreamServers.Where(p => p.ID.Equals(id, StringComparison.OrdinalIgnoreCase)).FirstOrDefault();
        }
    }
}