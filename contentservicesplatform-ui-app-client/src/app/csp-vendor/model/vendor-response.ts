import {VendorAsset} from "./vendor-asset";

export interface VendorResponse{
  viewAngleList: VendorAsset[];
  description: string;
  source: string;
  gtin: string;
  imageType: string;
  providedSize: string;
  background: string;
  lastModifiedDate: string;
}
