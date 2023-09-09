import React, {useState} from "react";

import Box from '@mui/material/Box';
import {DataGrid} from '@mui/x-data-grid';

const columns = [
    {field: 'id', headerName: 'Id', flex: 1, headerClassName: 'data-grid-header'},
    {field: 'fileName', headerName: 'File Name', flex: 1, headerClassName: 'data-grid-header'},
    {field: 'hcluster', headerName: 'Hierarchical Clustering', flex: 1, headerClassName: 'data-grid-header'},
    {field: 'kmeans', headerName: 'K-Means', flex: 1, headerClassName: 'data-grid-header'},
    {field: 'naiveBayes', headerName: 'Naive Bayes', flex: 1, headerClassName: 'data-grid-header'},
    {field: 'smo', headerName: 'SMO', flex: 1, headerClassName: 'data-grid-header'},
    {field: 'randomForest', headerName: 'Random Forest', flex: 1, headerClassName: 'data-grid-header'}
];

export default function DataSetCreatorGrid({activeData, totCount,pageSize,setPageSize}) {

    return (
        <>
            <Box item xs={12} sm={12} md={9}>
                <div>
                    <div style={{flexGrow: 1}}>
                        <DataGrid rows={activeData} className={'data-grid-data-row'}
                                  columns={columns}
                                  pageSize={pageSize}
                                  autoHeight {...activeData}
                                  onPageSizeChange={(newPageSize) => setPageSize(newPageSize)}
                                  pageSizeOptions={[5, 10, 25]}
                                  initialState={{
                                      ...activeData.initialState,
                                      pagination: {paginationModel: {pageSize: 5}},
                                  }} />
                    </div>
                </div>
            </Box>

        </>
    )
}