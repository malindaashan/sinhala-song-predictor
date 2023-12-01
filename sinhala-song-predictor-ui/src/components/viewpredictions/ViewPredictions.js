import React, {useEffect, useState} from "react";

import Box from '@mui/material/Box';
import {DataGrid} from '@mui/x-data-grid';
import ViewPredictionService from "../../service/ViewPredictionService";

const columns = [
    {field: 'id', headerName: 'Id', flex: 0.5, headerClassName: 'data-grid-header'},
    {field: 'fileName', headerName: 'File Name', flex: 1, headerClassName: 'data-grid-header'},
    {field: 'predLabel', headerName: 'Prediction Label', flex: 1, headerClassName: 'data-grid-header'},
    {field: 'score', headerName: 'score', flex: 0.5, headerClassName: 'data-grid-header'},
    {field: 'mlDistribution', headerName: 'ML Distribution', flex: 2, headerClassName: 'data-grid-header'},
    {field: 'nlpDistribution', headerName: 'NLP Distribution', flex: 2, headerClassName: 'data-grid-header'},
    {field: 'timestamp', headerName: 'Timestamp', flex: 2, headerClassName: 'data-grid-header'}
];

export default function ViewPredictions() {

    const [data, setData] = useState([]);

    useEffect(() => {
        findAllPredictions();
    }, []);

    function findAllPredictions() {
        ViewPredictionService.getAllPredictions()
            .then((response) => {
                if (response.success) {
                    setData(response.data);
                } else {
                    alert("Error Loading the data");
                }
            })
            .catch((ex) => {
                alert("Error Loading the data");
            });
    }

    return (
        <>
            <Box item xs={3} sm={3} md={6} lg={10}>
                <div>
                    <div style={{flexGrow: 1}}>
                        <DataGrid rows={data} className={'data-grid-data-row'}
                                  columns={columns}
                                  pageSizeOptions={[5, 10, 25]}
                                  paginationMode="server"/>
                    </div>
                </div>
            </Box>

        </>
    )
}