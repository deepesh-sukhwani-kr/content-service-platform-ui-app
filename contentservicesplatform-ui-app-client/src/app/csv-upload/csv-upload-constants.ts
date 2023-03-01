export enum CsvUploadConstants {
  REFERENCE_ID_PREFIX = 'DAP-UI-BULK-UPLOAD-',
  VALIDATION_PREFIX_ROW_NO = 'Validation error Row No.',
  VALIDATION_ERR_GTIN_LENGTH = '[Length of GTIN should be 14 characters] ',
  VALIDATION_ERR_GTIN_TYPE = '[GTIN should be in digits] ',
  VALIDATION_ERR_VIEW_ANGLE_MISSING = '[VIEW_ANGLE should be present] ',
  VALIDATION_ERR_SIZE_FORMAT = 'SIZE should be in the following format: HeightXWidth or HeightxWidth or Height*Width [Example: 1000X1000 or 100x100 or 10*10 ] ',
  VALIDATION_ERR_BACKGROUND_MISSING = '[BACKGROUND should be present] ',
  VALIDATION_ERR_LAST_MODIFIED_DATE_FORMAT = '[LAST_MODIFIED_DT should be in the following format (if Present): yyyy-mm-dd or mm/dd/yyyy] ',
  VALIDATION_ERR_DESCRIPTION_MISSING = '[DESCRIPTION should be present] ',
  VALIDATION_ERR_SOURCE_MISSING = '[SOURCE should be present] ',
  VALIDATION_ERR_IMAGE_TYPE_MISSING = '[IMAGE_TYPE should be present] ',
  VALIDATION_ERR_FILE_NAME_MISSING = '[FILE_NAME should be present] ',
  VALIDATION_ERR_COLOR_PROFILE_MISSING = '[COLOR_PROFILE should be present] ',
  VALIDATION_ERR_IMAGE_ORIENTATION_TYPE = '[IMAGE_ORIENTATION_TYPE should be present] ',
  VALIDATION_ERR_IMAGE_SWATCH = '[SOURCE should be IMP-SUPPORT-LEGACY-DS for Swatch] ',
  VALIDATION_ERR_SOURCE = '[Not a valid Source] ',
  UPLOAD_STATUS_REPORT_FORMAT = 'File Provided,Upload Status,Image ID,FILE_NAME,GTIN,VIEW_ANGLE,SIZE,BACKGROUND,LAST_MODIFIED_DT,DESCRIPTION,UPC_10,UPC_12,UPC_13,SOURCE,IMAGE_TYPE,COLOR_PROFILE,IMAGE_ORIENTATION_TYPE\r\n '
}
