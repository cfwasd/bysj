export interface Tree {
    id: number;
    label: string
    fileCount: number
    children?: Tree[]
}

export interface File{
    id: number;
    uuid: string;
    folder: string;
    folderId: number;
    fileRealName: string;
    fileRawName: string;
    fileName: string;
    fileExtension: string;
    ossUrl: string;
}
