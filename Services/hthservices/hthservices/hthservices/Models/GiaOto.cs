using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace hthservices.Models
{
    public class GiaOto
    {
        public string carId { get; set; }
        public string carName { get; set; }
        public string carType { get; set; }
        public string carBrand { get; set; }
        public string carOrigin { get; set; }
        public string carPrice { get; set; }
        public string carPriceDeviation { get; set; }
        public string carEngine { get; set; }
        public string carGear { get; set; }
        public string carPower { get; set; }
        public string carMoment { get; set; }
        public string carSize { get; set; }
        public string carFuelTankCapacity { get; set; }
        public string carGroundClearance { get; set; }
        public string carCompetitors { get; set; }
        public string carTurningCircle { get; set; }
        public string carImage { get; set; }
        public string shareUrl { get; set; }
        
        public string Image { get { return "https://i-vnexpress.vnecdn.net/" + carImage; } }
        public string Name { get { return carName; } }
        public string HangXe { get { return carBrand; } }
        public string LoaiXe { get { return carType; } }
        public string NguonGoc { get { return carOrigin; } }
        public float GiaNiemYet { get { float value = 0; float.TryParse(carPrice, out value); return value; } }
        public float GiaDamPhan { get { float value = 0; float.TryParse(carPriceDeviation, out value); return value; } }
        public string DongCo { get { return carGear; } }
        public int CongSuat { get { int value = 0; int.TryParse(carPower, out value); return value; } }
        public int Momen { get { int value = 0; int.TryParse(carMoment, out value); return value; } }
        public string Size { get { return carSize; } }
        public string DetailUrl { get { return "https://vnexpress.net/" + shareUrl; } }

    }

    public class OtoInfo
    {        

        public string Image { get; set; }
        public string Name { get; set; }
        public string HangXe { get; set; }
        public string LoaiXe { get; set; }
        public string NguonGoc { get; set; }
        public float GiaNiemYet { get; set; }
        public float GiaDamPhan { get; set; }
        public string DongCo { get; set; }
        public int CongSuat { get; set; }
        public int Momen { get; set; }
        public string Size { get; set; }
        public string DetailUrl { get; set; }
    }
}