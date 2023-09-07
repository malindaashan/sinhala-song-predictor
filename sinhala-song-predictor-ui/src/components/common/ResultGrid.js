import * as React from 'react';
import {Grid} from "@mui/material";

const ResultGrid = ({predictionResponse}) => {

    return (
        <Grid container justifyContent="center">
            <table>
                <tr>
                    <th colSpan={3}>
                        <h1>Result is : {predictionResponse != null ?
                            predictionResponse.predictedValue === 0 ? "Calm" : predictionResponse.predictedValue === 1 ? "Happy" :
                                predictionResponse.predictedValue === 2 ? "Sad" : "Error" : null}</h1>
                    </th>
                </tr>
                <tr>
                    <th colSpan={3}>
                        <h1>Distribution</h1>
                    </th>
                </tr>
                <tr>
                    <th>Calm</th>
                    <th>Happy</th>
                    <th>Sad</th>
                </tr>
                <tr>
                    {predictionResponse != null ?
                        <>
                            <td>{predictionResponse.predictedDistribution.Calm}</td>
                            <td>{predictionResponse.predictedDistribution.Happy}</td>
                            <td>{predictionResponse.predictedDistribution.Sad}</td>
                        </>
                        : null}
                </tr>
            </table>

        </Grid>

    );


}

export default ResultGrid;