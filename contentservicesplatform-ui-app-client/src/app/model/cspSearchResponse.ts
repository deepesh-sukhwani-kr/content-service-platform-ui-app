import {Image} from "./image";
import {CspErrorDetails} from "./cspErrorDetails";

export interface CspSearchResponse{
  images: Image[];
  error: CspErrorDetails;
}
