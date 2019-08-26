import {AssetIdentifier} from "./AssetIdentifier";
import {ImageObject} from "./ImageObject";

export class ImageAddRequest{
  referenceId: string;
  creationDatetime: string;
  imageType: string;
  assetIdentifier: AssetIdentifier;
  assetDetails: ImageObject[];
  constructor(){}
}
