import {AssetDetailResponse} from "./assetDetailResponse";
import {ErrorResponse} from "./ErrorResponse";

export class AddImageResponse{
  referenceId: string;
  creationDatetime: string;
  assetDetails: AssetDetailResponse[];
  errorResponse: ErrorResponse;
}
